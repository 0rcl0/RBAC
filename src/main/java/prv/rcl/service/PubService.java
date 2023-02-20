package prv.rcl.service;

import java.util.Optional;

public interface PubService<T,ID> {

    <S extends T> S  save(S entity);

    void delete(T entity);

    Optional<T> findById(ID id);



}
