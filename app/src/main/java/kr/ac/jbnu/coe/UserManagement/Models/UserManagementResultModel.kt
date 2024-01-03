package kr.ac.jbnu.coe.UserManagement.Models

enum class UserManagementResultModel {
    success, passwordMisMatch, invalidEmail, networkError, internalError, emptyField, weakPassword, emailAlreadyInUse, invalidStudent
}