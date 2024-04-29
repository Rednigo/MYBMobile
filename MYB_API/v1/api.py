from fastapi import APIRouter
from v1.endpoints import users, savings, incomes, categories, expenses

router = APIRouter()

router.include_router(users.router, prefix="/users", tags=["users"])
router.include_router(savings.router, prefix="/savings", tags=["savings"])
router.include_router(incomes.router, prefix="/incomes", tags=["incomes"])
router.include_router(categories.router, prefix="/categories", tags=["categories"])
router.include_router(expenses.router, prefix="/expenses", tags=["expenses"])
