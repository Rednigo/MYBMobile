import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from v1.api import router as v1_router
from core.settings import SERVER_HOST, SERVER_PORT, LOG_LEVEL, DEBUG_RELOAD, allowed_origins


app = FastAPI(title="Your Project API", version="1.0")


# @app.on_event("startup")
# async def startup_event():
#     # Example: Connect to database, create db tables, warm-up caches, etc.
#     create_tables()
#
#
# @app.on_event("shutdown")
# async def shutdown_event():
#     # Example: Disconnect from database, clean up, etc.
#     pass

# Including API routes from v1
app.include_router(v1_router, prefix="/api/v1")


# Setup CORS from settings
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # settings.allowed_origins,
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["*"],
)

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host=SERVER_HOST,
        port=SERVER_PORT,
        log_level=LOG_LEVEL,
        reload=DEBUG_RELOAD
    )

# can be started by clicking Run 'main'
# or
# uvicorn main:app --port 8080 --reload
# or
# uvicorn main:app --host 192.168.0.163 --port 8080 --reload
