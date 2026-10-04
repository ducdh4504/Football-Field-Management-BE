package com.prm393.footballfieldmanagement;

import com.prm393.footballfieldmanagement.entity.AIChatLog;
import com.prm393.footballfieldmanagement.entity.Booking;
import com.prm393.footballfieldmanagement.entity.CartItem;
import com.prm393.footballfieldmanagement.entity.Equipment;
import com.prm393.footballfieldmanagement.entity.EquipmentRental;
import com.prm393.footballfieldmanagement.entity.EquipmentRentalItem;
import com.prm393.footballfieldmanagement.entity.Facility;
import com.prm393.footballfieldmanagement.entity.FieldType;
import com.prm393.footballfieldmanagement.entity.FootballField;
import com.prm393.footballfieldmanagement.entity.Notification;
import com.prm393.footballfieldmanagement.entity.Order;
import com.prm393.footballfieldmanagement.entity.OrderItem;
import com.prm393.footballfieldmanagement.entity.Payment;
import com.prm393.footballfieldmanagement.entity.Product;
import com.prm393.footballfieldmanagement.entity.Referee;
import com.prm393.footballfieldmanagement.entity.ShoppingCart;
import com.prm393.footballfieldmanagement.entity.Standing;
import com.prm393.footballfieldmanagement.entity.Team;
import com.prm393.footballfieldmanagement.entity.Tournament;
import com.prm393.footballfieldmanagement.entity.TournamentMatch;
import com.prm393.footballfieldmanagement.entity.TournamentRegistration;
import com.prm393.footballfieldmanagement.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PersistenceModelStructureTest {

    private static final Set<Class<?>> ENTITY_CLASSES = Set.of(
            User.class, Facility.class, FieldType.class, FootballField.class, Referee.class, Booking.class,
            Payment.class, Notification.class, AIChatLog.class, Tournament.class, Team.class,
            TournamentRegistration.class, TournamentMatch.class, Standing.class, Equipment.class,
            EquipmentRental.class, EquipmentRentalItem.class, Product.class, ShoppingCart.class,
            CartItem.class, Order.class, OrderItem.class
    );

    @Test
    void mapsExactlyTheTwentyTwoApprovedBusinessTables() {
        Set<String> actualTableNames = ENTITY_CLASSES.stream()
                .map(entityClass -> entityClass.getAnnotation(Table.class).name())
                .collect(Collectors.toSet());

        Set<String> expectedTableNames = Set.of(
                "users", "facilities", "field_types", "football_fields", "referees", "bookings",
                "payments", "notifications", "ai_chat_logs", "tournaments", "teams",
                "tournament_registrations", "tournament_matches", "standings", "equipments",
                "equipment_rentals", "equipment_rental_items", "products", "shopping_carts",
                "cart_items", "orders", "order_items"
        );

        assertEquals(22, ENTITY_CLASSES.size());
        assertEquals(expectedTableNames, actualTableNames);
    }

    @Test
    void persistsEveryEnumAsAStringAndUsesNoManyToManyMapping() {
        ENTITY_CLASSES.forEach(entityClass -> Arrays.stream(entityClass.getDeclaredFields()).forEach(field -> {
            if (field.isAnnotationPresent(Enumerated.class)) {
                assertEquals(EnumType.STRING, field.getAnnotation(Enumerated.class).value());
            }
            assertFalse(field.isAnnotationPresent(ManyToMany.class));
        }));
    }
}
