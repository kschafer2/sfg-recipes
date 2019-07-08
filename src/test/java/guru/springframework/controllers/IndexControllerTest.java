package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    IndexController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    public void getIndexPage() throws Exception {

        //given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        //make sure both recipes are not equal
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipes.add(recipe);

        //verify getRecipes returns recipes
        when(recipeService.getRecipes()).thenReturn(recipes);

        //create an ArgumentCaptor for the Set class
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.getIndexPage(model);

        //then
        //verify getIndexPage returns "index"
        assertEquals("index", viewName);

        //verify getRecipes in recipeService is called only once
        verify(recipeService, times(1)).getRecipes();

        //verify adding one attribute named "recipes" and capture the argument in argumentCaptor
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());

        //verify both recipes are in the captured set
        Set<Recipe> setIndexController = argumentCaptor.getValue();
        assertEquals(2, setIndexController.size());
    }
}