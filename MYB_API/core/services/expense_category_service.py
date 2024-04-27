from typing import List
from sqlalchemy.orm import Session
from core.models.models import ExpenseCategory
from core.repositories.expense_category_repository import (
    db_create_expense_category,
    db_get_expense_category,
    db_get_all_expense_categories_by_user_id,
    db_update_expense_category,
    db_delete_expense_category
)
from core.schemas.expense_category_schemas import ExpenseCategoryCreateSchema, ExpenseCategoryUpdateSchema


def create_expense_category(db: Session, category: ExpenseCategoryCreateSchema, user_id: int):
    return db_create_expense_category(db=db, category=category, user_id=user_id)


def get_expense_categories_by_user_id(db: Session, user_id: int, skip: int = 0, limit: int = 100) -> List[ExpenseCategory]:
    return db_get_all_expense_categories_by_user_id(db=db, user_id=user_id, skip=skip, limit=limit)


def get_expense_category_by_id(db: Session, category_id: int):
    return db_get_expense_category(db=db, category_id=category_id)


def update_expense_category(db: Session, category_id: int, category: ExpenseCategoryUpdateSchema):
    return db_update_expense_category(db=db, category_id=category_id, category=category)


def delete_expense_category(db: Session, category_id: int):
    return db_delete_expense_category(db=db, category_id=category_id)
