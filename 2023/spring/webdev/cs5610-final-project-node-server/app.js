import express from "express";
import cors from "cors";
import UsersController from "./users/users-controller.js";
import mongoose from "mongoose";

mongoose.connect(
    "mongodb+srv://giuseppi:supersecretpassword@cluster0.kwknyiw.mongodb.net/?retryWrites=true&w=majority"
);

const app = express();
app.use(cors());
app.use(express.json());

app.get("/", function (req, res) {
    res.send("Hello World");
});

UsersController(app);

app.get("/hello/:message", function (req, res) {
    const message = req.params.message;
    res.send(`Hello ${message}`);
});

app.listen(4000);