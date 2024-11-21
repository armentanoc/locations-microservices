package br.ucsal.domain.locations;

import br.ucsal.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "locations")
public class Location extends BaseEntity {

    @Column(nullable = false)
    private LocationType locationType;

    private String description;

    private Long capacity;

    @Column(nullable = false)
    private String location;

    private String resources;

    protected Location() {
        // default for JPA
    }

    public Location(LocationType locationType, String description, Long capacity, String location, String resources) {
        this.locationType = locationType;
        this.description = description;
        this.capacity = capacity;
        this.location = location;
        this.resources = resources;
    }

    public Location(Location location) {
        super();
        this.locationType = location.locationType;
        this.description = location.description;
        this.capacity = location.capacity;
        this.location = location.location;
        this.resources = location.resources;
    }

    public Location copy() {
        return new Location(this);
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        if(locationType != null) {
            this.locationType = locationType;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description != null) {
            this.description = description;
        }
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        if(capacity != null) {
            this.capacity = capacity;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if(location != null) {
            this.location = location;
        }
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        if(resources != null) {
            this.resources = resources;
        }
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationType=" + locationType +
                ", description='" + description + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                ", resources='" + resources + '\'' +
                '}';
    }
}
