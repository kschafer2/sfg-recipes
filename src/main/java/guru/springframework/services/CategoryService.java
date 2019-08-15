package guru.springframework.services;

import guru.springframework.commands.CategoryCommand;

import java.util.Set;

public interface CategoryService {

    Set<CategoryCommand> listAllCategories();

}
