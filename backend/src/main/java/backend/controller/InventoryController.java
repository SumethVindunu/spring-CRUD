package backend.controller;

import backend.model.InventoryModel;
import backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@CrossOrigin("http://localhost:3000")
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    // @GetMapping("/")
    // public String rootMap(){
    //     String message = "Hello World";
    //     return message;
    // }

    @PostMapping("/Inventory")
    public InventoryModel newInventoryModel(@RequestBody InventoryModel newInventoryModel){

        return inventoryRepository.save(newInventoryModel);

    }
    @PostMapping("inventory/itemImg")
    public String itemImage(@RequestParam("file")MultipartFile file){
        String folder = "src/main/uploads/";
        String itemImage = file.getOriginalFilename();

        try {
            File uplodDir = new File(folder);
            if (!uplodDir.exists()){
                uplodDir.mkdir();
            }
            file.transferTo(Paths.get(folder+itemImage));
        }catch (IOException e){
            e.printStackTrace();
            return "Error uploading file: "+ itemImage;
        }
        return itemImage;
    }
    
}
