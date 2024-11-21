package br.ucsal.infrastructure;

import br.ucsal.domain.locations.Location;
import br.ucsal.domain.locations.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long>  {
    public List<Optional<Location>> findAllBylocationType(LocationType locationType);
    List<Location> findAllByOrderByIdAsc();
}
