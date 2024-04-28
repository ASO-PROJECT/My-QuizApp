package com.example.myquiizapp

data class QuizModel(

    val id: String, // Unique identifier for the quiz
    val title: String, // Title of the quiz
    val subtitle: String, // Subtitle or additional information about the quiz
    val time: String, // Time duration for the quiz
    val questionList: List<QuestionModel> // List of questions in the quiz
) {
    // Secondary constructor with default values
    constructor() : this("", "", "", "", emptyList())
}

// Data class representing a question
data class QuestionModel(
    val question: String, // The question text
    val options: List<String>, // List of options for the question
    val correct: String // The correct answer to the question
) {
    // Secondary constructor with default values
    constructor() : this("", emptyList(), "")
}



















