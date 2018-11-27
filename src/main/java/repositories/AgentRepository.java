
package repositories;

import domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {

    @Query("select a from Agent a where a.userAccount.active=true")
    Collection<Agent> findAllActive();
}
