package com.saly.user.common.auth;

import java.util.Optional;

public interface JwtUserDetailsFetcher {
    Optional<SalyUserDetails> fetchUserDetails(String token);
}
