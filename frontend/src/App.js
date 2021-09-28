import './App.css';
import GroupsPage from "./pages/GroupsPage";
import PlayersPage from "./pages/PlayersPage";
import PlayersListPage from "./pages/PlayersListPage";
import TournamentPage from "./pages/TournamentPage";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

export default function App() {

    return (
        <Router>
            <Switch>
                <Route path="/newTournament" component={TournamentPage}/>
                <Route path="/groups/:number" component={GroupsPage}/>
                <Route path="/groups/:number" component={GroupsPage}/>
                <Route path="/playerEditPage" component={PlayerEditPage}/>
                <Route path="/playersList" component={PlayersListPage}/>
            </Switch>
        </Router>
    );
}