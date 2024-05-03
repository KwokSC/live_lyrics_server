package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.LyricDTO;
import com.chunkie.live_lyrics_server.dto.ProgramDTO;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.entity.Programme;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    @Resource
    private ProgrammeRepository programmeRepository;

    @Resource
    private SongService songService;

    @Resource
    private S3Service s3Service;

    private final String ALBUM_IMAGE_PREFIX = "resources/images/album/";
    private final String LRC_PREFIX = "resources/lyric/";
    private final String AUDIO_PREFIX = "resources/audio/";
    private final String RECOMMENDATION_PREFIX = "resources/images/recommendation/";

    private final static Logger logger = LoggerFactory.getLogger(SongService.class);

    public boolean uploadAudio(MultipartFile audio) {
        return s3Service.uploadFile(AUDIO_PREFIX + audio.getOriginalFilename(), audio);
    }

    public boolean uploadAlbumCover(MultipartFile image) {
        return s3Service.uploadFile(ALBUM_IMAGE_PREFIX + image.getOriginalFilename(), image);
    }

    public boolean uploadLyric(List<MultipartFile> lyric) {
        for (MultipartFile file : lyric) {
            String filename = file.getOriginalFilename();
            assert filename != null;
            String id = filename.substring(filename.lastIndexOf("_") + 1);
            if (!s3Service.uploadFile(LRC_PREFIX + id + "/" + file.getOriginalFilename(), file))
                return false;
        }
        return true;
    }

//    public boolean uploadImages(String programId, List<MultipartFile> images) {
//        for (MultipartFile file : images) {
//            String filename = file.getOriginalFilename();
//            assert filename != null;
//            if (!s3Service.uploadFile(RECOMMENDATION_PREFIX + programId + "/" + file.getOriginalFilename(), file))
//                return false;
//        }
//        return true;
//    }

    public String getAlbumCoverById(String songId) {
        String id = songId.substring(songId.lastIndexOf("_") + 1);
        return s3Service.getFile(ALBUM_IMAGE_PREFIX + "album_" + id);
    }

    public String getAudioById(String songId) {
        String id = songId.substring(songId.lastIndexOf("_") + 1);
        return s3Service.getFile(AUDIO_PREFIX + "audio_" + id);
    }

    public List<LyricDTO> getLyricsById(String songId) {
        return s3Service.getLyricsById(LRC_PREFIX + songId.substring(songId.indexOf("_") + 1) + "/");
    }



    public ProgrammeDTO getProgrammeByRoomId(String roomId) {
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        ProgrammeDTO programmeDTO = new ProgrammeDTO();
        if (programme == null) {
            Programme newProgramme = new Programme(roomId, new ArrayList<>());
            newProgramme.setProgrammeId(roomId);
            programmeRepository.save(newProgramme);
            return null;
        }
        programmeDTO.setProgrammeId(programme.getProgrammeId());
        for(Program program : programme.getProgramList()){
            Song song = songService.getSongById(program.getSongId());
            if(song == null){
                continue;
            }
            ProgramDTO programDTO = new ProgramDTO();
            programDTO.setSong(song);
//            for(Recommendation recommendation : program.getRecommendations()){
//                if (recommendation.getType().equals("Image")){
//
//                }
//            }
            programDTO.setRecommendations(program.getRecommendations());
            programmeDTO.getProgramList().add(programDTO);
        }
        return programmeDTO;
    }

    public boolean addProgramByRoomId(String roomId, Program program) {
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        if (programme == null) {
            Programme newProgramme = new Programme(roomId, new ArrayList<>());
            newProgramme.getProgramList().add(program);
            programmeRepository.save(newProgramme);
            return true;
        }
        programme.getProgramList().add(program);
        programmeRepository.save(programme);
        return true;
    }

    public boolean deleteProgramById(String roomId, String songId) {
        String id = songId.replace("song_", "");
        s3Service.deleteFile(AUDIO_PREFIX + "audio_" + id);
        s3Service.deleteFile(ALBUM_IMAGE_PREFIX + "album_" + id);
        s3Service.deleteFile(LRC_PREFIX + id);
        songService.deleteSongById(songId);
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        if (programme != null) {
            List<Program> programList =
                    programme.getProgramList().stream().filter(program -> !program.getSongId().equals(songId)).collect(Collectors.toList());
            programme.setProgramList(programList);
            programmeRepository.save(programme);
            return true;
        }
        return false;
    }
}
