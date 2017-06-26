package entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private String name;
	@NotNull
    private String cpf;
	@NotNull
    private String email;
	
    
	public Customer(Long id, String name, String cpf, String email) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;				
		this.email = email;
	}
	public Customer(Long id){
		this.id = id;
	}
	public Customer(String name, String cpf, String email) {
		
		this.name = name;
		this.cpf = cpf;				
		this.email = email;
	}
	public Customer(String name, String cpf) {			
		this.name = name;
		this.cpf = cpf;	
	}
	public Customer(){}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
    public String toString() {
        return String.format(
                "Cliente[id=%d, name='%s', cpf='%s' , email='%s']",

                id, name, cpf, email);
    }

   
}