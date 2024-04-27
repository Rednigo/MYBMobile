from typing import List
from sqlalchemy.orm import Session
from core.models.models import Savings
from core.repositories.savings_repository import (
    db_create_savings,
    db_get_savings,
    db_get_savings_by_user_id,
    db_update_savings,
    db_delete_savings
)
from core.schemas.savings_schemas import SavingsCreateSchema, SavingsUpdateSchema


def create_savings(db: Session, savings: SavingsCreateSchema, user_id: int):
    return db_create_savings(db=db, savings=savings, user_id=user_id)


def get_savings_by_user_id(db: Session, user_id: int, skip: int = 0, limit: int = 100) -> List[Savings]:
    return db_get_savings_by_user_id(db=db, skip=skip, limit=limit, user_id=user_id)


def get_savings_by_id(db: Session, savings_id: int):
    return db_get_savings(db=db, savings_id=savings_id)


def update_savings(db: Session, savings_id: int, savings: SavingsUpdateSchema):
    return db_update_savings(db=db, savings_id=savings_id, savings=savings)


def delete_savings(db: Session, savings_id: int):
    return db_delete_savings(db=db, savings_id=savings_id)
