package eu.kanade.domain.category.interactor

import eu.kanade.domain.category.model.Category
import eu.kanade.domain.category.model.CategoryUpdate
import eu.kanade.domain.category.repository.CategoryRepository
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.ui.library.setting.LibrarySort
import eu.kanade.tachiyomi.ui.library.setting.plus

class SetSortModeForCategory(
    private val preferences: PreferencesHelper,
    private val categoryRepository: CategoryRepository,
) {

    suspend fun await(categoryId: Long, type: LibrarySort.Type, direction: LibrarySort.Direction) {
        val category = categoryRepository.get(categoryId) ?: return
        val flags = category.flags + type + direction
        if (preferences.categorizedDisplaySettings().get()) {
            categoryRepository.updatePartial(
                CategoryUpdate(
                    id = category.id,
                    flags = flags,
                ),
            )
        } else {
            preferences.librarySortingMode().set(LibrarySort(type, direction))
            categoryRepository.updateAllFlags(flags)
        }
    }

    suspend fun await(category: Category, type: LibrarySort.Type, direction: LibrarySort.Direction) {
        await(category.id, type, direction)
    }
}
