from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from core.dependencies.dependencies import get_db
from core.services.expense_category_service import (
    create_expense_category,
    get_expense_categories_by_user_id,
    get_expense_category_by_id,
    update_expense_category,
    delete_expense_category
)
from core.schemas.expense_category_schemas import ExpenseCategoryCreateSchema, ExpenseCategorySchema, ExpenseCategoryUpdateSchema


router = APIRouter()


@router.post("/expense-categories", response_model=ExpenseCategorySchema)
def create_expense_category_endpoint(category: ExpenseCategoryCreateSchema, db: Session = Depends(get_db)):
    return create_expense_category(db=db, category=category)


@router.get("/expense-categories", response_model=List[ExpenseCategorySchema])
def get_expense_categories_endpoint(user_id: int, db: Session = Depends(get_db)):
    return get_expense_categories_by_user_id(db=db, user_id=user_id)


@router.get("/expense-categories/{category_id}", response_model=ExpenseCategorySchema)
def get_expense_category_by_id_endpoint(category_id: int, db: Session = Depends(get_db)):
    category = get_expense_category_by_id(db=db, category_id=category_id)
    if category is None:
        raise HTTPException(status_code=404, detail="Expense category not found")
    return category


@router.put("/expense-categories", response_model=ExpenseCategorySchema)
def update_expense_category_endpoint(category_id: int, category: ExpenseCategoryUpdateSchema, db: Session = Depends(get_db)):
    return update_expense_category(db=db, category_id=category_id, category=category)


@router.delete("/expense-categories", response_model=ExpenseCategorySchema)
def delete_expense_category_endpoint(category_id: int, db: Session = Depends(get_db)):
    return delete_expense_category(db=db, category_id=category_id)
