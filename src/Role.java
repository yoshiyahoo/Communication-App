/**
 * Role is all the possible roles/permissions of a user.
 * EMPLOYEE: Can only see and type in chats they are a member in.
 * ADMINISTRATOR: Can see and type in any created chat. (You are not safe).
 */
public enum Role {
    ADMINISTRATOR,
    EMPLOYEE
}
