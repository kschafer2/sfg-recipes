package guru.springframework.services;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.converters.CategoryToCategoryCommand;
import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    CategoryToCategoryCommand categoryToCategoryCommand;
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    public CategoryServiceImplTest() {
        this.categoryToCategoryCommand = new CategoryToCategoryCommand();
    }

    @Before
    public void SetUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryService = new CategoryServiceImpl(categoryRepository,
                                                    categoryToCategoryCommand);
    }

    @Test
    public void listAllCategories() throws Exception {
        //given
        Set<Category> categories = new HashSet<>();

        Category category = new Category();
        category.setId(1L);
        categories.add(category);

        Category category2 = new Category();
        category2.setId(2L);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        //when
        Set<CategoryCommand> commands = categoryService.listAllCategories();

        //then
        assertEquals(2, commands.size());
        verify(categoryRepository, times(1)).findAll();
      }
}
