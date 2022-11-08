package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.Activity;
import com.example.balancewellspringboot.model.Goal;
import com.example.balancewellspringboot.model.Profile;
import com.example.balancewellspringboot.model.dto.ProfileDTO;
import com.example.balancewellspringboot.service.interfaces.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/add")
    public String saveProfile(ProfileDTO profileDTO, @RequestPart List<MultipartFile> images, HttpServletRequest request) throws IOException {
        List<String> imagesNames= Collections.singletonList(this.saveImage(images.get(0)));
        profileService.save(profileDTO, imagesNames);

        return "redirect:/profile/my-profile/"+request.getRemoteUser();
    }

    @GetMapping("/my-profile/{username}")
    public String getProfileForUsername(@PathVariable String username, Model model) {
        Profile profile = profileService.getProfileForUsername(username);
        String latestImage = profile.getEndUser().getImages().get(0).getTitle();
        model.addAttribute("profile", profile);
        model.addAttribute("image", latestImage);

        return "my-profile";
    }

    @GetMapping("/edit/{id}")
    public String editProfile(@PathVariable Long id, Model model) {
        Profile profileToEdit = profileService.findById(id);
        model.addAttribute("profile", profileToEdit);
        model.addAttribute("goals", getGoals());
        model.addAttribute("activities", getActivities());
        return "edit-profile";
    }

    private String saveImage(MultipartFile img) throws IOException {
        String imgName = img.getOriginalFilename();
        File fileToUpload = new File("uploads/" + imgName);
        fileToUpload.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileToUpload);
        fileOutputStream.write(img.getBytes());
        fileOutputStream.close();
        return img.getOriginalFilename();
    }

    private List<String> getGoals() {
        return Arrays.stream(Goal.values()).map(Goal::toString).collect(Collectors.toList());
    }

    private List<String> getActivities() {
        return Arrays.stream(Activity.values()).map(Activity::toString).collect(Collectors.toList());
    }
}
