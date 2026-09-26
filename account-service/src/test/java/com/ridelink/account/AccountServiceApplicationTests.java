package com.ridelink.account;

import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.UpdateProfileRequest;
import com.ridelink.account.dto.UpdateRoleRequest;
import com.ridelink.account.dto.UpdateStatusRequest;
import com.ridelink.account.model.Account;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.model.Role;
import com.ridelink.account.repository.AccountRepository;
import com.ridelink.account.security.JwtService;
import com.ridelink.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ridelink.account.exception.InvalidCredentialsException;
import com.ridelink.account.exception.AccountNotActiveException;
import com.ridelink.account.exception.AccountNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceApplicationTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void register_shouldCreateAccountSuccessfully() {

        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Silva");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setPhone("0712345678");
        request.setRole(Role.PASSENGER);

        when(accountRepository.existsByEmail("john@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("hashedPassword");

        Account savedAccount = new Account(
                "John Silva",
                "john@example.com",
                "hashedPassword",
                "0712345678",
                Role.PASSENGER,
                AccountStatus.ACTIVE
        );

        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        Account result = accountService.register(request);

        assertNotNull(result);
        assertEquals("John Silva", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals("0712345678", result.getPhone());
        assertEquals(Role.PASSENGER, result.getRole());
        assertEquals(AccountStatus.ACTIVE, result.getStatus());

        verify(accountRepository).existsByEmail("john@example.com");
        verify(passwordEncoder).encode("password123");
        verify(accountRepository).save(any(Account.class));
    }

	@Test
	void register_shouldRejectDuplicateEmail() {

		RegisterRequest request = new RegisterRequest();
		request.setFullName("John Silva");
		request.setEmail("john@example.com");
		request.setPassword("password123");
		request.setPhone("0712345678");
		request.setRole(Role.PASSENGER);

		when(accountRepository.existsByEmail("john@example.com"))
				.thenReturn(true);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> accountService.register(request)
		);

		assertEquals("Email already registered", exception.getMessage());

		verify(accountRepository).existsByEmail("john@example.com");
		verify(passwordEncoder, never()).encode(anyString());
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void register_shouldRejectSecondAdmin() {

		RegisterRequest request = new RegisterRequest();
		request.setFullName("Another Admin");
		request.setEmail("admin2@example.com");
		request.setPassword("password123");
		request.setPhone("0712345678");
		request.setRole(Role.ADMIN);

		when(accountRepository.existsByEmail("admin2@example.com"))
				.thenReturn(false);

		when(accountRepository.existsByRole(Role.ADMIN))
				.thenReturn(true);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> accountService.register(request)
		);

		assertEquals(
				"An ADMIN account already exists",
				exception.getMessage()
		);

		verify(accountRepository).existsByEmail("admin2@example.com");
		verify(accountRepository).existsByRole(Role.ADMIN);
		verify(passwordEncoder, never()).encode(anyString());
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void login_shouldReturnTokenForValidCredentials() {

		LoginRequest request = new LoginRequest();
		request.setEmail("john@example.com");
		request.setPassword("password123");

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0712345678",
				Role.PASSENGER,
				AccountStatus.ACTIVE
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		when(passwordEncoder.matches("password123", "hashedPassword"))
				.thenReturn(true);

		when(jwtService.generateToken(account))
				.thenReturn("test-jwt-token");

		String result = accountService.login(request);

		assertNotNull(result);
		assertEquals("test-jwt-token", result);

		verify(accountRepository).findByEmail("john@example.com");
		verify(passwordEncoder).matches("password123", "hashedPassword");
		verify(jwtService).generateToken(account);
	}

	@Test
	void login_shouldRejectWrongPassword() {

		LoginRequest request = new LoginRequest();
		request.setEmail("john@example.com");
		request.setPassword("wrongPassword");

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0712345678",
				Role.PASSENGER,
				AccountStatus.ACTIVE
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		when(passwordEncoder.matches("wrongPassword", "hashedPassword"))
				.thenReturn(false);

		InvalidCredentialsException exception = assertThrows(
				InvalidCredentialsException.class,
				() -> accountService.login(request)
		);

		assertEquals(
				"Invalid email or password",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("john@example.com");
		verify(passwordEncoder).matches("wrongPassword", "hashedPassword");
		verify(jwtService, never()).generateToken(any(Account.class));
	}

	@Test
	void login_shouldRejectUnknownEmail() {

		LoginRequest request = new LoginRequest();
		request.setEmail("unknown@example.com");
		request.setPassword("password123");

		when(accountRepository.findByEmail("unknown@example.com"))
				.thenReturn(java.util.Optional.empty());

		InvalidCredentialsException exception = assertThrows(
				InvalidCredentialsException.class,
				() -> accountService.login(request)
		);

		assertEquals(
				"Invalid email or password",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("unknown@example.com");
		verify(passwordEncoder, never()).matches(anyString(), anyString());
		verify(jwtService, never()).generateToken(any(Account.class));
	}

	@Test
	void login_shouldRejectInactiveAccount() {

		LoginRequest request = new LoginRequest();
		request.setEmail("john@example.com");
		request.setPassword("password123");

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0712345678",
				Role.PASSENGER,
				AccountStatus.SUSPENDED
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		when(passwordEncoder.matches("password123", "hashedPassword"))
				.thenReturn(true);

		AccountNotActiveException exception = assertThrows(
				AccountNotActiveException.class,
				() -> accountService.login(request)
		);

		assertEquals(
				"Account is not active",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("john@example.com");
		verify(passwordEncoder).matches("password123", "hashedPassword");
		verify(jwtService, never()).generateToken(any(Account.class));
	}

	@Test
	void getByEmail_shouldReturnAccountWhenFound() {

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0712345678",
				Role.PASSENGER,
				AccountStatus.ACTIVE
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		Account result = accountService.getByEmail("john@example.com");

		assertNotNull(result);
		assertEquals("John Silva", result.getFullName());
		assertEquals("john@example.com", result.getEmail());
		assertEquals(Role.PASSENGER, result.getRole());
		assertEquals(AccountStatus.ACTIVE, result.getStatus());

		verify(accountRepository).findByEmail("john@example.com");
	}

	@Test
	void getByEmail_shouldThrowExceptionWhenAccountNotFound() {

		when(accountRepository.findByEmail("unknown@example.com"))
				.thenReturn(java.util.Optional.empty());

		AccountNotFoundException exception = assertThrows(
				AccountNotFoundException.class,
				() -> accountService.getByEmail("unknown@example.com")
		);

		assertEquals(
				"Account not found",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("unknown@example.com");
	}

	@Test
	void updateProfile_shouldUpdateAccountSuccessfully() {

		UpdateProfileRequest request = new UpdateProfileRequest();
		request.setFullName("John Silva Updated");
		request.setPhone("0712345678");

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0700000000",
				Role.PASSENGER,
				AccountStatus.ACTIVE
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		when(accountRepository.save(any(Account.class)))
				.thenReturn(account);

		Account result = accountService.updateProfile(
				"john@example.com",
				request
		);

		assertNotNull(result);
		assertEquals("John Silva Updated", result.getFullName());
		assertEquals("0712345678", result.getPhone());

		verify(accountRepository).findByEmail("john@example.com");
		verify(accountRepository).save(account);
	}

	@Test
	void updateProfile_shouldThrowExceptionWhenAccountNotFound() {

		UpdateProfileRequest request = new UpdateProfileRequest();
		request.setFullName("John Silva Updated");
		request.setPhone("0712345678");

		when(accountRepository.findByEmail("unknown@example.com"))
				.thenReturn(java.util.Optional.empty());

		AccountNotFoundException exception = assertThrows(
				AccountNotFoundException.class,
				() -> accountService.updateProfile(
						"unknown@example.com",
						request
				)
		);

		assertEquals(
				"Account not found",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("unknown@example.com");
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void updateRole_shouldUpdateRoleSuccessfully() {

		UpdateRoleRequest request = new UpdateRoleRequest();
		request.setRole(Role.DRIVER);

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0712345678",
				Role.PASSENGER,
				AccountStatus.ACTIVE
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		when(accountRepository.save(any(Account.class)))
				.thenReturn(account);

		Account result = accountService.updateRole(
				"john@example.com",
				request
		);

		assertNotNull(result);
		assertEquals(Role.DRIVER, result.getRole());

		verify(accountRepository).findByEmail("john@example.com");
		verify(accountRepository).save(account);
	}

	@Test
	void updateRole_shouldThrowExceptionWhenAccountNotFound() {

		UpdateRoleRequest request = new UpdateRoleRequest();
		request.setRole(Role.DRIVER);

		when(accountRepository.findByEmail("unknown@example.com"))
				.thenReturn(java.util.Optional.empty());

		AccountNotFoundException exception = assertThrows(
				AccountNotFoundException.class,
				() -> accountService.updateRole(
						"unknown@example.com",
						request
				)
		);

		assertEquals(
				"Account not found",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("unknown@example.com");
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void updateStatus_shouldUpdateStatusSuccessfully() {

		UpdateStatusRequest request = new UpdateStatusRequest();
		request.setStatus(AccountStatus.SUSPENDED);

		Account account = new Account(
				"John Silva",
				"john@example.com",
				"hashedPassword",
				"0712345678",
				Role.PASSENGER,
				AccountStatus.ACTIVE
		);

		when(accountRepository.findByEmail("john@example.com"))
				.thenReturn(java.util.Optional.of(account));

		when(accountRepository.save(any(Account.class)))
				.thenReturn(account);

		Account result = accountService.updateStatus(
				"john@example.com",
				request
		);

		assertNotNull(result);
		assertEquals(AccountStatus.SUSPENDED, result.getStatus());

		verify(accountRepository).findByEmail("john@example.com");
		verify(accountRepository).save(account);
	}

	@Test
	void updateStatus_shouldThrowExceptionWhenAccountNotFound() {

		UpdateStatusRequest request = new UpdateStatusRequest();
		request.setStatus(AccountStatus.SUSPENDED);

		when(accountRepository.findByEmail("unknown@example.com"))
				.thenReturn(java.util.Optional.empty());

		AccountNotFoundException exception = assertThrows(
				AccountNotFoundException.class,
				() -> accountService.updateStatus(
						"unknown@example.com",
						request
				)
		);

		assertEquals(
				"Account not found",
				exception.getMessage()
		);

		verify(accountRepository).findByEmail("unknown@example.com");
		verify(accountRepository, never()).save(any(Account.class));
	}
}
