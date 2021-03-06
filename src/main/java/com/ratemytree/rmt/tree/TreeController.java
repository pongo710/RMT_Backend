package com.ratemytree.rmt.tree;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * David Schilling - davejs92@gmail.com
 */
@Controller
@RequestMapping(value = "/trees")
public class TreeController {

    private final TreeService treeService;

    @Autowired
    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Tree> createTree(@RequestBody String tree) {
        Tree savedTree = treeService.create(tree);
        return new ResponseEntity<>(savedTree, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Tree> getTree(@PathVariable String id) {
        Tree tree = treeService.findById(id);
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/up", method = RequestMethod.POST)
    public ResponseEntity<Tree> voteUpForTree(@PathVariable String id) {
        Tree updatedTree = treeService.voteForTree(id, true);
        return new ResponseEntity<>(updatedTree, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/down", method = RequestMethod.POST)
    public ResponseEntity<Tree> voteDownForTree(@PathVariable String id) {
        Tree updatedTree = treeService.voteForTree(id, false);
        return new ResponseEntity<>(updatedTree, HttpStatus.OK);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Tree>> getTreesByVotes() {
        List<Tree> trees = treeService.findTreesByVotes();
        return new ResponseEntity<>(trees, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, params = {"limit", "orderBy"})
    public ResponseEntity<List<Tree>> getTrees(@RequestParam int limit, @RequestParam String orderBy) {
        List<Tree> trees = treeService.findTrees(orderBy, limit);
        return new ResponseEntity<>(trees, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/myvote", method = RequestMethod.GET)
    public ResponseEntity<TreeVote> getMyVote(@PathVariable String id){
        TreeVote treeVote = treeService.getCurrentUserVote(id);
        return new ResponseEntity<>(treeVote, HttpStatus.OK);
    }
}
