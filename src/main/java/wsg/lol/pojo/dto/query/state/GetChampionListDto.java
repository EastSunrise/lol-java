package wsg.lol.pojo.dto.query.state;

import wsg.lol.pojo.base.QueryDto;

/**
 * wsg
 *
 * @author wangsigen
 */
public class GetChampionListDto extends QueryDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
