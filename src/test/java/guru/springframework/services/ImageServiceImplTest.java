package guru.springframework.services;

import guru.springframework.commands.ImageCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.BytesUnwrappedToBytesWrapped;
import guru.springframework.converters.BytesWrappedToBytesUnwrapped;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    BytesUnwrappedToBytesWrapped bytesUnwrappedToBytesWrapped;
    BytesWrappedToBytesUnwrapped bytesWrappedToBytesUnwrapped;
    ImageService imageService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        bytesUnwrappedToBytesWrapped = new BytesUnwrappedToBytesWrapped();
        bytesWrappedToBytesUnwrapped = new BytesWrappedToBytesUnwrapped();

        imageService = new ImageServiceImpl(recipeRepository,
                                            bytesUnwrappedToBytesWrapped,
                                            bytesWrappedToBytesUnwrapped);
    }

    @Test
    public void saveImageFile() throws Exception {
        //given
        Long id = 1L;
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "Spring Framework Guru".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(id,multipartFile);

        //then
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().getImageBytes().length);
    }

    @Test
    public void getImageByteArrayOrDefault() throws Exception {
        //given
        ImageCommand imageCommand = new ImageCommand();
        Byte[] imageBytes = bytesUnwrappedToBytesWrapped.convert("fake image bytes".getBytes());
        imageCommand.setImageBytes(imageBytes);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setImage(imageCommand);

        //when
        byte[] fetchedBytes = imageService.getImageBytesFromRecipeOrDefault(recipeCommand, "foo.bar");

        //then
        assertNotNull(imageBytes);
        assertEquals(imageBytes.length, fetchedBytes.length);
    }

    @Test
    public void getImageByteArrayOrDefaultNull() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        String defaultImagePath = "src/main/resources/static/images/default.jpg";

        //when
        byte[] fetchedBytes = imageService.getImageBytesFromRecipeOrDefault(recipeCommand, defaultImagePath);

        //then
        assertNull(recipeCommand.getImage());
        assertNotNull(fetchedBytes);
    }
}