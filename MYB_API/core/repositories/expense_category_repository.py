from sqlalchemy.orm import Session
from core.models.models import ExpenseCategory
from core.schemas.expense_category_schemas import ExpenseCategoryCreateSchema, ExpenseCategoryUpdateSchema


def db_get_expense_category(db: Session, category_id: int):
    return db.query(ExpenseCategory).filter(ExpenseCategory.id == category_id).first()


def db_get_all_expense_categories_by_user_id(db: Session, user_id: int, skip: int = 0, limit: int = 100):
    return db.query(ExpenseCategory).filter(ExpenseCategory.user_id == user_id).offset(skip).limit(limit).all()


def db_create_expense_category(db: Session, category: ExpenseCategoryCreateSchema):
    db_category = ExpenseCategory(**category.dict())
    db.add(db_category)
    db.commit()
    db.refresh(db_category)
    return db_category


def db_update_expense_category(db: Session, category_id: int, category: ExpenseCategoryUpdateSchema):
    db_category = db_get_expense_category(db, category_id)
    if db_category:
        update_data = category.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_category, key, value)
        db.commit()
        db.refresh(db_category)
    return db_category


def db_delete_expense_category(db: Session, category_id: int):
    db_category = db_get_expense_category(db, category_id)
    if db_category:
        db.delete(db_category)
        db.commit()
    return db_category
