from pydantic import BaseModel


class ExpenseCategoryBase(BaseModel):
    category_name: str
    amount: float
    user_id: int


class ExpenseCategoryUpdateSchema(BaseModel):
    category_name: str | None = None
    amount: float | None = None


class ExpenseCategoryCreateSchema(ExpenseCategoryBase):
    pass


class ExpenseCategorySchema(ExpenseCategoryBase):
    id: int
