package com.histcat.parameterized.test;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgumentAggregatorTest {
    @ParameterizedTest
    @CsvSource({
            "zhang, san, M, 1999-01-01",
    })
    void testWithArgumentAccessor(ArgumentsAccessor accessor) {
        Person person = new Person(accessor.getString(0),
                accessor.getString(1),
                accessor.get(2, Gender.class),
                accessor.get(3, LocalDate.class));
        assertEquals("zhang", person.firstName);
        assertEquals("san", person.lastName);
        assertEquals(Gender.M, person.gender);
        assertEquals(LocalDate.of(1999, 1, 1), person.birthday);
    }

    @ParameterizedTest
    @CsvSource({
            "zhang, san, M, 1999-01-01",
    })
    void testWithArgumentAggregator(@Csv2Person Person person) {
        assertEquals("zhang", person.firstName);
        assertEquals("san", person.lastName);
        assertEquals(Gender.M, person.gender);
        assertEquals(LocalDate.of(1999, 1, 1), person.birthday);
    }

    private static class Person {
        private final String firstName;
        private final String lastName;
        private final Gender gender;
        private final LocalDate birthday;

        Person(String firstName, String lastName, Gender gender, LocalDate birthday) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.birthday = birthday;
        }
    }

    private enum Gender {
        M
    }

    private static class MyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new Person(accessor.getString(0),
                    accessor.getString(1),
                    accessor.get(2, Gender.class),
                    accessor.get(3, LocalDate.class));
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AggregateWith(MyAggregator.class)
    private @interface Csv2Person {

    }
}
