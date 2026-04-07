import {App, Button, Empty, Form, Input, Modal, Table} from "antd";
import {DeleteOutlined} from "@ant-design/icons";
import {FC, useState} from "react";
import {useBooks, useCreateBook, useDeleteBook} from "../hooks/book.hook.ts";
import {Book} from "../api/book.api.types.ts";

export const HomePage: FC = () => {
    const {data: books} = useBooks();
    const {message} = App.useApp();
    const [showCreateNewModal, setShowCreateNewModal] = useState<boolean>(false);
    const [bookToDelete, setBookToDelete] = useState<Book | null>(null);
    const [form] = Form.useForm();

    const closeModal = () => {
        form.resetFields();
        setShowCreateNewModal(false);
    };

    const addBook = useCreateBook({
        onSuccess: closeModal,
        onValidationError: (errors) => form.setFields(
            Object.entries(errors).map(([field, msg]) => ({name: field, errors: [msg]})),
        ),
        onDetailedError: (detail) => void message.error(detail),
        onError: () => void message.error('Something went wrong. Please try again.'),
    });

    const deleteBook = useDeleteBook({
        onSuccess: () => {
            setBookToDelete(null);
            void message.success('Book deleted successfully');
        },
        onError: (detail) => void message.error(`Failed to delete book: ${detail}`),
    });

    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
        },
        {
            title: 'Author',
            dataIndex: 'author',
            key: 'author',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            key: 'actions',
            render: (_: unknown, record: Book) => (
                <DeleteOutlined data-testid="delete-book" onClick={() => setBookToDelete(record)}/>
            ),
        },
    ];

    return (
        <>
            <h1>Books</h1>
            <Table dataSource={books?.items} columns={columns} rowKey="id" showHeader={true} locale={{
                emptyText: (
                    <Empty
                        image={Empty.PRESENTED_IMAGE_SIMPLE}
                        description="No Books Available"
                        data-testid="no-books-message"
                    />
                ),
            }} data-testid="books-table"></Table>
            <Button data-testid="add-books" onClick={() => setShowCreateNewModal(true)}>Add Book</Button>
            <Modal
                title={'Add new Book'}
                open={showCreateNewModal}
                onCancel={closeModal}
                okText="Add"
                confirmLoading={addBook.isPending}
                onOk={async () => {
                    const values = await form.validateFields().catch(() => null);
                    if (!values) return;
                    addBook.mutate(values);
                }}
            >
                <Form form={form} autoComplete="off">
                    <Form.Item label={'Title'} htmlFor="title" name="title" rules={[
                        {required: true, whitespace: true, message: 'Title is required'},
                        {max: 255, message: 'Title must be at most 255 characters'},
                    ]}>
                        <Input id="title" aria-label="title"/>
                    </Form.Item>
                    <Form.Item label={'Author'} htmlFor="author" name="author" rules={[
                        {required: true, whitespace: true, message: 'Author is required'},
                        {max: 255, message: 'Author must be at most 255 characters'},
                    ]}>
                        <Input id="author" aria-label="author"/>
                    </Form.Item>
                    <Form.Item label={'Description'} htmlFor="description" name="description" rules={[
                        {required: true, whitespace: true, message: 'Description is required'},
                        {max: 5000, message: 'Description must be at most 5000 characters'},
                    ]}>
                        <Input.TextArea id="description" aria-label="description" rows={4}/>
                    </Form.Item>
                </Form>
            </Modal>
            <Modal
                title="Delete Book"
                open={bookToDelete !== null}
                onCancel={() => setBookToDelete(null)}
                confirmLoading={deleteBook.isPending}
                onOk={() => {
                    if (bookToDelete) {
                        deleteBook.mutate(bookToDelete.id);
                    }
                }}
            >
                <p>{`Are you sure you want to delete '${bookToDelete?.title}'?`}</p>
            </Modal>
        </>
    );
};
