package nl.nn.workshop;

import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, EnrollmentPk> {

}
