package wsg.lol.dao.mybatis.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import wsg.lol.common.utils.EnumUtil;
import wsg.lol.pojo.base.BaseEnum;
import wsg.lol.pojo.enums.impl.code.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * wsg
 *
 * @author wangsigen
 */
@MappedTypes({
        DivisionEnum.class, GameModeEnum.class, GameTypeEnum.class, MapEnum.class, MatchLaneEnum.class,
        MatchQueueEnum.class, MatchRoleEnum.class, PlatformEnum.class, PositionEnum.class, RankQueueEnum.class,
        SeasonEnum.class, TierEnum.class
})
public class CodeEnumTypeHandler<E extends Enum & BaseEnum<Integer>> extends BaseTypeHandler<E> {

    private Class<E> clazz;

    public CodeEnumTypeHandler(Class<E> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.clazz = clazz;
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int index, E e, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            preparedStatement.setObject(index, e.getValue());
        } else {
            preparedStatement.setObject(index, e.getValue(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Integer value = resultSet.getInt(columnName);
        return resultSet.wasNull() ? null : EnumUtil.parseFromValue(value, this.clazz);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        Integer value = resultSet.getInt(columnIndex);
        return resultSet.wasNull() ? null : EnumUtil.parseFromValue(value, this.clazz);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        Integer value = callableStatement.getInt(columnIndex);
        return callableStatement.wasNull() ? null : EnumUtil.parseFromValue(value, this.clazz);
    }
}