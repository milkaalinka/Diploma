package com.alinaincorporated.diploma.ui.transactions

import android.content.res.Resources
import com.alinaincorporated.diploma.R

class TransactionCategoryMapper(
    private val resources: Resources,
) {

    companion object {
        const val ID_INCOME_SALARY = 1
        const val ID_INCOME_PENSION = 2
        const val ID_INCOME_SCOLARSHIP = 3
        const val ID_INCOME_BONUS = 4
        const val ID_EXPENSE_FOOD = 5
        const val ID_EXPENSE_HOME = 6
        const val ID_EXPENSE_ENTERTAINMENT = 7
        const val ID_EXPENSE_GIFTS = 8
        const val ID_EXPENSE_HEALTH = 9
        const val ID_EXPENSE_CLOTHING = 10
        const val ID_EXPENSE_EDUCATION = 11
        const val ID_EXPENSE_OTHER = 12
    }

    private val categories: List<String> by lazy {
        resources.getStringArray(R.array.incomeCategories).toList() +
            resources.getStringArray(R.array.expenseCategories).toList()
    }

    fun mapToId(category: String): Int = with(resources) {
        when (category) {
            getString(R.string.category_income_salary) -> ID_INCOME_SALARY
            getString(R.string.category_income_pension) -> ID_INCOME_PENSION
            getString(R.string.category_income_scolarship) -> ID_INCOME_SCOLARSHIP
            getString(R.string.category_income_bonus) -> ID_INCOME_BONUS
            getString(R.string.category_expense_food) -> ID_EXPENSE_FOOD
            getString(R.string.category_expense_home) -> ID_EXPENSE_HOME
            getString(R.string.category_expense_entertainment) -> ID_EXPENSE_ENTERTAINMENT
            getString(R.string.category_expense_gifts) -> ID_EXPENSE_GIFTS
            getString(R.string.category_expense_health) -> ID_EXPENSE_HEALTH
            getString(R.string.category_expense_clothing) -> ID_EXPENSE_CLOTHING
            getString(R.string.category_expense_education) -> ID_EXPENSE_EDUCATION
            else -> error("Unsupported category name!")
        }
    }

    fun mapFromId(id: Int): String = with(resources) {
        return when (id) {
            ID_INCOME_SALARY -> getString(R.string.category_income_salary)
            ID_INCOME_PENSION -> getString(R.string.category_income_pension)
            ID_INCOME_SCOLARSHIP -> getString(R.string.category_income_scolarship)
            ID_INCOME_BONUS -> getString(R.string.category_income_bonus)
            ID_EXPENSE_FOOD -> getString(R.string.category_expense_food)
            ID_EXPENSE_HOME -> getString(R.string.category_expense_home)
            ID_EXPENSE_ENTERTAINMENT -> getString(R.string.category_expense_entertainment)
            ID_EXPENSE_GIFTS -> getString(R.string.category_expense_gifts)
            ID_EXPENSE_HEALTH -> getString(R.string.category_expense_health)
            ID_EXPENSE_CLOTHING -> getString(R.string.category_expense_clothing)
            ID_EXPENSE_EDUCATION -> getString(R.string.category_expense_education)
            else -> error("Unsupported category id!")
        }
    }
}