# Git Workflow

This project uses a simplified Git workflow with two main branches:

## Branches

### `main` (Development)
- **Purpose**: Active development branch
- **Protection**: All changes must pass CI tests before merging
- **Usage**: All feature branches should be merged into `main`
- **Default branch**: This is the default branch for the repository

### `prod` (Production)
- **Purpose**: Production-ready code
- **Protection**: Only merges from `main` after thorough testing
- **Usage**: Deployed to production environments
- **Updates**: Merged from `main` when ready for release

## Workflow

### 1. Feature Development
```bash
# Create a new feature branch from main
git checkout main
git pull origin main
git checkout -b feature/your-feature-name

# Make your changes
git add .
git commit -m "feat: add your feature"

# Push and create PR
git push origin feature/your-feature-name
```

### 2. Merge to Main
- Create a Pull Request from your feature branch to `main`
- Ensure all CI checks pass
- Get code review approval
- Merge to `main`

### 3. Deploy to Production
```bash
# When ready to deploy, merge main to prod
git checkout prod
git pull origin prod
git merge main
git push origin prod
```

Or create a Pull Request from `main` to `prod` for review.

## CI/CD

### CI (Continuous Integration)
- Runs on every push to `main` and `prod`
- Includes: linting, type checking, and tests
- Must pass before merging

### Production Deployment
- Runs on every push to `prod`
- Runs full test suite with coverage checks
- Creates deployment tags
- Requires 75%+ test coverage

## Branch Protection Rules

### `main` Branch
- Require pull request reviews
- Require status checks to pass
- Require branches to be up to date
- Restrict who can push

### `prod` Branch
- Require pull request reviews
- Require status checks to pass
- Require branches to be up to date
- Restrict who can push (maintainers only)

## Release Process

1. **Feature Complete**: All features merged to `main`
2. **Testing**: Thorough testing on `main` branch
3. **Release PR**: Create PR from `main` to `prod`
4. **Review**: Code review and final testing
5. **Merge**: Merge to `prod` (triggers deployment)
6. **Tag**: Automatic tag creation on deployment

## Emergency Hotfixes

For critical production issues:

```bash
# Create hotfix branch from prod
git checkout prod
git pull origin prod
git checkout -b hotfix/critical-fix

# Make the fix
git add .
git commit -m "fix: critical production issue"

# Merge to prod directly (bypass main for emergencies)
git checkout prod
git merge hotfix/critical-fix
git push origin prod

# Also merge back to main to keep branches in sync
git checkout main
git merge hotfix/critical-fix
git push origin main
```

## Best Practices

1. **Commit Messages**: Use conventional commits (`feat:`, `fix:`, `docs:`, etc.)
2. **Branch Names**: Use descriptive names (`feature/`, `fix/`, `hotfix/`)
3. **Small Commits**: Keep commits focused and atomic
4. **Pull Requests**: Always use PRs for merging (no direct pushes)
5. **Code Review**: Get at least one review before merging
6. **Tests**: Ensure all tests pass before merging
7. **Documentation**: Update docs when changing behavior

## Commands Reference

```bash
# Check current branch
git branch

# Switch branches
git checkout main
git checkout prod

# Create new branch
git checkout -b feature/my-feature

# Merge branches
git merge feature/my-feature

# Push to remote
git push origin main
git push origin prod

# Pull latest changes
git pull origin main
git pull origin prod

# View branch history
git log --oneline --graph --all
```
