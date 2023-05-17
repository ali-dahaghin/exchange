package ir.iau.exchange.dto.data;

import lombok.Data;

import java.util.List;

@Data
public class DashboardDataDto {

    private String username;
    private String name;
    private List<UserAssetDataDto> assets;

}
