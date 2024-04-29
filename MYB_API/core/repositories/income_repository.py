from sqlalchemy.orm import Session
from core.models.models import Income
from core.schemas.income_schemas import IncomeCreateSchema, IncomeUpdateSchema


def db_get_income(db: Session, income_id: int):
    return db.query(Income).filter(Income.id == income_id).first()


def db_get_all_incomes_by_user_id(db: Session, user_id: int, skip: int = 0, limit: int = 100):
    return db.query(Income).filter(Income.user_id == user_id).offset(skip).limit(limit).all()


def db_create_income(db: Session, income: IncomeCreateSchema):
    db_income = Income(**income.dict())
    db.add(db_income)
    db.commit()
    db.refresh(db_income)
    return db_income


def db_update_income(db: Session, income_id: int, income: IncomeUpdateSchema):
    db_income = db_get_income(db, income_id)
    if db_income:
        update_data = income.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_income, key, value)
        db.commit()
        db.refresh(db_income)
    return db_income


def db_delete_income(db: Session, income_id: int):
    db_income = db_get_income(db, income_id)
    if db_income:
        db.delete(db_income)
        db.commit()
    return db_income
