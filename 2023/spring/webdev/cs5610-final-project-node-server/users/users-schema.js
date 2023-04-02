import mongoose from "mongoose";


const usersSchema = new mongoose.Schema(
    {
        username: { type: String, unique: true, required: true },
        password: { type: String, required: true },
        firstName: String,
        lastName: String,
        email: { type: String, unique: true },
        age: Number,
        booksRead: { type: Number, default: 0 },
        role: { type: String, default: "VIEWER", enum: ["VIEWER", "AUTHOR", "CRITIC", "ADMIN"] },
    },
    {
        collection: "users",
    }
);
export default usersSchema;