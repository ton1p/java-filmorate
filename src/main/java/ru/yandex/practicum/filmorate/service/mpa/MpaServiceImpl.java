package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private MpaStorage mpaStorage;

    @Override
    public Collection<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa getById(int id) {
        Optional<Mpa> mpa = mpaStorage.getById(id);
        return mpa.orElseThrow(() -> new NotFoundException("Mpa with id " + id + " not found"));
    }
}
