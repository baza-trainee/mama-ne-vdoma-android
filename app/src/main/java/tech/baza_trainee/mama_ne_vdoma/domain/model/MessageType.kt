package tech.baza_trainee.mama_ne_vdoma.domain.model

enum class MessageType(val type: String) {
    JOIN("join"), ACCEPT("accept"), REJECT("reject"),
    KICK("kick"), MAKE_ADMIN("make_admin")
}