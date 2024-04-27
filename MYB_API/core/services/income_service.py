from typing import List
from sqlalchemy.orm import Session
from core.models.models import Income
from core.repositories.income_repository import (
    db_create_income,
    db_get_income,
    db_get_all_incomes_by_user_id,
    db_update_income,
    db_delete_income
)
from core.schemas.income_schemas import IncomeCreateSchema, IncomeUpdateSchema


def create_income(db: Session, income: IncomeCreateSchema, user_id: int):
    return db_create_income(db=db, income=income, user_id=user_id)


def get_incomes_by_user_id(db: Session, user_id: int, skip: int = 0, limit: int = 100) -> List[Income]:
    return db_get_all_incomes_by_user_id(db=db, user_id=user_id, skip=skip, limit=limit)


def get_income_by_id(db: Session, income_id: int):
    return db_get_income(db=db, income_id=income_id)


def update_income(db: Session, income_id: int, income: IncomeUpdateSchema):
    return db_update_income(db=db, income_id=income_id, income=income)


def delete_income(db: Session, income_id: int):
    return db_delete_income(db=db, income_id=income_id)
