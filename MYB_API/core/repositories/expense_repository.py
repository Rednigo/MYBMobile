from datetime import datetime
from sqlalchemy.orm import Session
from core.models.models import Expense
from core.schemas.expense_schemas import ExpenseCreateSchema, ExpenseUpdateSchema


def db_get_expense(db: Session, expense_id: int):
    return db.query(Expense).filter(Expense.id == expense_id).first()


def db_get_all_expenses_by_category_id(db: Session, category_id: int, skip: int = 0, limit: int = 100):
    return db.query(Expense).filter(Expense.category_id == category_id).offset(skip).limit(limit).all()


def db_create_expense(db: Session, expense: ExpenseCreateSchema, user_id: int, category_id: int):
    db_expense = Expense(
        expense_name=expense.expense_name,
        amount=expense.amount,
        date=expense.date or datetime.utcnow(),
        user_id=user_id,
        category_id=category_id
    )
    db.add(db_expense)
    db.commit()
    db.refresh(db_expense)
    return db_expense


def db_update_expense(db: Session, expense_id: int, expense: ExpenseUpdateSchema):
    db_expense = db_get_expense(db, expense_id)
    if db_expense:
        update_data = expense.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_expense, key, value)
        db.commit()
        db.refresh(db_expense)
    return db_expense


def db_delete_expense(db: Session, expense_id: int):
    db_expense = db_get_expense(db, expense_id)
    if db_expense:
        db.delete(db_expense)
        db.commit()
    return db_expense

