package com.example.acronymdefinitionapp.utils

class NullResponseException(message: String = "Error happened here") : Exception(message)
class FailureResponse(message: String) : Exception(message)