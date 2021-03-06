package com.ratemytree.rmt.tree;

import com.ratemytree.rmt.EntityNotFoundException;
import com.ratemytree.rmt.user.AuthenticationService;
import com.ratemytree.rmt.user.User;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * David Schilling - davejs92@gmail.com
 */
@Service
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public TreeServiceImpl(TreeRepository treeRepository, AuthenticationService authenticationService) {
        this.treeRepository = treeRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Tree create(String content) {
        return treeRepository.save(new Tree(content, authenticationService.getCurrentlyLoggedIn().getUsername(), new DateTime()));
    }

    @Override
    public Tree save(Tree tree) {
        return treeRepository.save(tree);
    }

    @Override
    public Tree findById(String id) {
        Tree tree = treeRepository.findOne(id);
        if (tree == null) {
            throw new EntityNotFoundException("Tree with id " + id + " does not exist.");
        }
        return tree;
    }

    @Override
    public Tree voteForTree(String id, boolean up) {
        Tree tree = treeRepository.findOne(id);
        User currentUser = authenticationService.getCurrentlyLoggedIn();
        tree.addVoter(new TreeVote(currentUser.getUsername(), up));
        if (up) {
            tree.incrementVotesUp();
        } else {
            tree.incrementVotesDown();
        }
        return treeRepository.save(tree);
    }

    @Override
    public List<Tree> findTreesByVotes() {

        return findTrees("votesUp", 100);
    }

    @Override
    public List<Tree> findTrees(String orderBy, int limit) {
        if (Tree.getPossibleOrders().contains(orderBy)) {
            Pageable pageable = new PageRequest(0, limit, new Sort(DESC, orderBy));
            return treeRepository.findAll(pageable).getContent();
        }
        throw new IllegalArgumentException("orderBy: " + orderBy + " is not possible. Possible are: " +
                Tree.getPossibleOrders());
    }

    @Override
    public TreeVote getCurrentUserVote(String treeId) {
        Tree tree = treeRepository.findOne(treeId);
        User currentUser = authenticationService.getCurrentlyLoggedIn();

        TreeVote treeVote = tree.getVoteForUser(currentUser.getUsername());
        if (treeVote == null) {
            throw new EntityNotFoundException("No votes for user " + currentUser.getUsername());
        }
        return treeVote;
    }

    @Override
    public List<Tree> findByCreator(String creator) {

        return treeRepository.findByCreator(creator);
    }
}
