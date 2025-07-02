package com.milla.kartingrmbackend.services;

import com.milla.kartingrmbackend.entities.PeopleDiscountEntity;
import com.milla.kartingrmbackend.repositories.PeopleDiscountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleDiscountServiceTest {

    @Mock
    private PeopleDiscountRepository peopleDiscountRepository;

    @InjectMocks
    private PeopleDiscountService peopleDiscountService;

    private PeopleDiscountEntity sampleDiscount;

    @BeforeEach
    void setUp() {
        sampleDiscount = new PeopleDiscountEntity();
        sampleDiscount.setPeopleDiscountId(1);
        sampleDiscount.setMinPeople(3);
        sampleDiscount.setMaxPeople(5);
        sampleDiscount.setDiscount(new BigDecimal("0.15"));
    }

    @Test
    void getAllShouldReturnListOfDiscounts() {
        // Arrange
        List<PeopleDiscountEntity> expectedDiscounts = Arrays.asList(sampleDiscount);
        when(peopleDiscountRepository.findAll()).thenReturn(expectedDiscounts);

        // Act
        List<PeopleDiscountEntity> result = peopleDiscountService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(expectedDiscounts.size(), result.size());
        assertEquals(sampleDiscount.getMinPeople(), result.get(0).getMinPeople());
        assertEquals(sampleDiscount.getMaxPeople(), result.get(0).getMaxPeople());
        assertEquals(sampleDiscount.getDiscount(), result.get(0).getDiscount());
        verify(peopleDiscountRepository).findAll();
    }

    @Test
    void getPeopleDiscountByIdShouldReturnDiscountWhenExists() {
        // Arrange
        when(peopleDiscountRepository.findById(1)).thenReturn(Optional.of(sampleDiscount));

        // Act
        PeopleDiscountEntity result = peopleDiscountService.getPeopleDiscountById(1);

        // Assert
        assertNotNull(result);
        assertEquals(sampleDiscount.getPeopleDiscountId(), result.getPeopleDiscountId());
        assertEquals(sampleDiscount.getMinPeople(), result.getMinPeople());
        assertEquals(sampleDiscount.getMaxPeople(), result.getMaxPeople());
        assertEquals(sampleDiscount.getDiscount(), result.getDiscount());
        verify(peopleDiscountRepository).findById(1);
    }

    @Test
    void getPeopleDiscountByIdShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(peopleDiscountRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                peopleDiscountService.getPeopleDiscountById(999)
        );
        assertEquals("PeopleDiscount not found with id: 999", exception.getMessage());
        verify(peopleDiscountRepository).findById(999);
    }

    @Test
    void saveShouldReturnSavedDiscount() {
        // Arrange
        PeopleDiscountEntity discountToSave = new PeopleDiscountEntity();
        discountToSave.setMinPeople(3);
        discountToSave.setMaxPeople(5);
        discountToSave.setDiscount(new BigDecimal("0.15"));

        when(peopleDiscountRepository.save(any(PeopleDiscountEntity.class)))
                .thenReturn(sampleDiscount);

        // Act
        PeopleDiscountEntity result = peopleDiscountService.save(discountToSave);

        // Assert
        assertNotNull(result);
        assertEquals(sampleDiscount.getPeopleDiscountId(), result.getPeopleDiscountId());
        assertEquals(sampleDiscount.getMinPeople(), result.getMinPeople());
        assertEquals(sampleDiscount.getMaxPeople(), result.getMaxPeople());
        assertEquals(sampleDiscount.getDiscount(), result.getDiscount());
        verify(peopleDiscountRepository).save(discountToSave);
    }

    @Test
    void updateShouldReturnUpdatedDiscount() {
        // Arrange
        PeopleDiscountEntity discountToUpdate = new PeopleDiscountEntity();
        discountToUpdate.setPeopleDiscountId(1);
        discountToUpdate.setMinPeople(4);
        discountToUpdate.setMaxPeople(6);
        discountToUpdate.setDiscount(new BigDecimal("0.20"));

        when(peopleDiscountRepository.save(any(PeopleDiscountEntity.class)))
                .thenReturn(discountToUpdate);

        // Act
        PeopleDiscountEntity result = peopleDiscountService.update(discountToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(discountToUpdate.getPeopleDiscountId(), result.getPeopleDiscountId());
        assertEquals(discountToUpdate.getMinPeople(), result.getMinPeople());
        assertEquals(discountToUpdate.getMaxPeople(), result.getMaxPeople());
        assertEquals(discountToUpdate.getDiscount(), result.getDiscount());
        verify(peopleDiscountRepository).save(discountToUpdate);
    }

    @Test
    void findByPeopleAmountShouldReturnMatchingDiscount() {
        // Arrange
        int peopleAmount = 4; // Amount between minPeople and maxPeople
        when(peopleDiscountRepository.findByPeopleAmount(peopleAmount))
                .thenReturn(sampleDiscount);

        // Act
        PeopleDiscountEntity result = peopleDiscountService.findByPeopleAmount(peopleAmount);

        // Assert
        assertNotNull(result);
        assertEquals(sampleDiscount.getPeopleDiscountId(), result.getPeopleDiscountId());
        assertEquals(sampleDiscount.getMinPeople(), result.getMinPeople());
        assertEquals(sampleDiscount.getMaxPeople(), result.getMaxPeople());
        assertEquals(sampleDiscount.getDiscount(), result.getDiscount());
        verify(peopleDiscountRepository).findByPeopleAmount(peopleAmount);
    }
}