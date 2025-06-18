package com.milla.KartingRMBackend.DTO;

import com.milla.KartingRMBackend.Entities.RentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentRequestDTO {
    private RentEntity rent;
    private List<String> subClients;

}
