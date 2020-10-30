package test;

import com.yy.platform.common.model.wonderful.NewNavSubscribe;
import com.yy.platform.common.model.wonderful.NewNavSubscribeExample;
import com.yy.platform.dao.mapper.wonderful.NewNavSubscribeMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@MapperScan({"com.yy.platform.dao.mapper.wonderful"})
@Component
public class TestService {

    @Autowired
    NewNavSubscribeMapper mapper;

    public List<NewNavSubscribe> getAll() {
        return mapper.selectByExample(new NewNavSubscribeExample());
    }
}
