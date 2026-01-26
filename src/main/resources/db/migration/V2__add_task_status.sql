-- Add status column to tasks table
ALTER TABLE tasks
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'TODO'
CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE'));

-- Create index for status filtering
CREATE INDEX idx_tasks_status ON tasks(status);

-- Create composite index for owner + status queries
CREATE INDEX idx_tasks_owner_status ON tasks(owner_id, status);

-- Update existing completed tasks to DONE status
UPDATE tasks
SET status = 'DONE'
WHERE completed = true;

-- Add comment for documentation
COMMENT ON COLUMN tasks.status IS 'Current status of the task: TODO, IN_PROGRESS, or DONE';
