package guru.springframework.controllers;

import guru.springframework.commands.ImageCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.BytesUnwrappedToBytesWrapped;
import guru.springframework.converters.BytesWrappedToBytesUnwrapped;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void getImageForm() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.getCommandById(anyLong())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getCommandById(anyLong());
    }

    @Test
    public void handleImagePost() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any());
    }

    @Test
    public void renderImageFromDB() throws Exception {
        //given
        BytesUnwrappedToBytesWrapped bytesUnwrappedToBytesWrapped = new BytesUnwrappedToBytesWrapped();
        BytesWrappedToBytesUnwrapped bytesWrappedToBytesUnwrapped = new BytesWrappedToBytesUnwrapped();
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        byte[] fakeImageBytes = "fake image text".getBytes();

        Byte[] bytesBoxed = new BytesUnwrappedToBytesWrapped().convert(fakeImageBytes);

        ImageCommand imageCommand = new ImageCommand();
        imageCommand.setImageBytes(bytesBoxed);

        command.setImage(imageCommand);

        when(recipeService.getCommandById(anyLong())).thenReturn(command);

        when(imageService.getImageBytesFromRecipeOrDefault(any(), anyString()))
                .thenReturn(bytesWrappedToBytesUnwrapped.convert(imageCommand.getImageBytes()));

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(fakeImageBytes.length, responseBytes.length);
    }

    @Test
    public void testGetImageNumberFormatError() throws Exception {
        mockMvc.perform(get("/recipe/asdf/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}
