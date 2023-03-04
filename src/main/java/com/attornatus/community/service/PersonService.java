
package com.attornatus.community.service;


import com.attornatus.community.mappers.PersonDetailMapper;

import com.attornatus.community.model.dto.request.PersonRequestDto;
import com.attornatus.community.model.dto.response.ListPeopleResponseDto;
import com.attornatus.community.model.dto.response.PersonResponseDetailsDto;
import com.attornatus.community.model.entity.Address;
import com.attornatus.community.model.entity.Person;
import com.attornatus.community.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private PersonDetailMapper mapperDetail;
    
    public PersonResponseDetailsDto create(PersonRequestDto request){
        
        Person person = mapperDetail.map(request);    
        personRepository.save(person);
       
        return mapperDetail.map(person);
    }
    
    public ListPeopleResponseDto findAll(){
        
        List<Person> people = personRepository.findAll();
        
        ListPeopleResponseDto peopleResponse = new ListPeopleResponseDto();
        peopleResponse.setListPeople(mapperDetail.map(people));
        
        return peopleResponse;
    }
    
    public void delete(Integer id) throws Exception {

        findByID(id);
        personRepository.deleteById(id);

    }
    
    public PersonResponseDetailsDto update(PersonRequestDto request,Integer id ) throws Exception{
        
        Person person = findByID(id);
        
        person.setName(request.getName());
        
        
        
        person.setBirthdate(request.getBirthdate());
        
        Long idAddresResponse = request.getIdAddress();
        
        Address address = person.getAddress().stream()
                .filter(a -> a.getId().equals(idAddresResponse))
                .findFirst()
                .orElse(null);
             
        person.setAddress((List<Address>) address);
  
        personRepository.save(person);
        return mapperDetail.map(person);
    }

    private Person findByID(Integer id) throws Exception {
        
        Optional<Person> person = personRepository.findById(id);
        
        if (person.isPresent()) {
            return person.get();
        } else {
           throw new Exception("O usuário não está registrado."); 

        }
    }
    
    public PersonResponseDetailsDto getResponseById(Integer id) throws Exception{
        
     return mapperDetail.map(findByID(id));   
    }

    //favorite
   
    public void favorite(Person person, Address address){
        
        List <Address> addresses = person.getAddress();
        
        for (Address a : addresses) {
            if(a.equals(address)){
                a.setFavorite(true);
            }else{
                a.setFavorite(false);
            }
        }
    }
}


//Hacer método con formateo de fecha en llamar en el Mapper