package org.example.mvcjsonview.entity;

public class UserViews {
    public interface UserSummary {} // Только данные о пользователе
    public interface UserDetails extends UserSummary {} // Данные о пользователе и его заказах
}
