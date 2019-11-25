package wsg.lol.common.pojo.dto.match;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wsg.lol.common.base.BaseDto;

/**
 * Bean for reference of masteries of the participant.
 *
 * @author Kingen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MasteryDto extends BaseDto {

    private Integer masteryId;
    private Integer rank;
}
