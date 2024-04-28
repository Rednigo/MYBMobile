from typing import List
from sqlalchemy.orm import Session
from core.models.models import Expense
from core.repositories.expense_repository import (
    db_create_expense,
    db_get_expense,
    db_get_all_expenses_by_category_id,
    db_update_expense,
    db_delete_expense
)
from core.schemas.expense_schemas import ExpenseCreateSchema, ExpenseUpdateSchema


def create_expense(db: Session, expense: ExpenseCreateSchema):
    return db_create_expense(db=db, expense=expense)


def get_expenses_by_category_id(db: Session, category_id: int, skip: int = 0, limit: int = 100) -> List[Expense]:
    return db_get_all_expenses_by_category_id(db=db, category_id=category_id, skip=skip, limit=limit)


def get_expense_by_id(db: Session, expense_id: int):
    return db_get_expense(db=db, expense_id=expense_id)


def update_expense(db: Session, expense_id: int, expense: ExpenseUpdateSchema):
    return db_update_expense(db=db, expense_id=expense_id, expense=expense)


def delete_expense(db: Session, expense_id: int):
    return db_delete_expense(db=db, expense_id=expense_id)
