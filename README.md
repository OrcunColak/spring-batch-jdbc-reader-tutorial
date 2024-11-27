# Read Me First

The original idea is from  
https://germanoschneider.medium.com/chunk-vs-tasklet-which-one-should-i-use-4d3e94be6b8a

# Tasklet vs Chunk

Chunk allows use to create a job that will execute in separate batches. This way, the application will manage and
process the data with little memory consumption.

When we work with the chunk concept, we should attribute a reader, processor, and writer.
