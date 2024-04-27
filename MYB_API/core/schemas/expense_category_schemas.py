from datetime import datetime
from pydantic import BaseModel


class ExpenseCategoryBase(BaseModel):
    name: str
    amount: float
    user_id: int


class ExpenseCategoryUpdateSchema(BaseModel):
    name: str | None = None
    amount: float | None = None
    category_id: int | None = None


class ExpenseCategoryCreateSchema(ExpenseCategoryBase):
    pass


class ExpenseCategorySchema(ExpenseCategoryBase):
    id: int
