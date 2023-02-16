package shop.mtcoding.bankapp.model.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import shop.mtcoding.bankapp.dto.user.JoinReqDto;

@Mapper
public interface UserRepository {
    public int insert(JoinReqDto joinReqDto);

    public int updateById(User user);

    public int deleteById(int id);

    public List<User> findAll();

    public User findById(int id);
}
